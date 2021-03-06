/*
 * Copyright 2010 Kim A. Betti, Alexey Zhokhov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package grails.plugin.sitemapper.impl;

import grails.plugin.sitemapper.EntryWriter;
import grails.plugin.sitemapper.SitemapServerUrlResolver;
import grails.plugin.sitemapper.Sitemapper;
import grails.util.Holders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Properties;

/**
 * @author <a href='mailto:kim@developer-b.com'>Kim A. Betti</a>
 */
public class XmlSitemapWriter extends AbstractSitemapWriter {

    private final static Logger log = LoggerFactory.getLogger(XmlSitemapWriter.class);

    private final static String SITEMAP_OPEN = "<sitemap>";
    private final static String SITEMAP_CLOSE = "</sitemap>\n";

    public String path;
    public String extension;

    public XmlSitemapWriter() {
        Properties properties = Holders.getConfig().toProperties();

        path = properties.getProperty("sitemap.prefix");
        extension = properties.getProperty("sitemap.gzip").equals("true") ? "xml.gz" : "xml";
    }

    @Override
    public void writeIndexEntries(PrintWriter writer) throws IOException {
        writeIndexHead(writer);

        SitemapDateUtils dateUtils = new SitemapDateUtils();

        for (String mapperName : sitemappers.keySet()) {
            Sitemapper mapper = sitemappers.get(mapperName);

            String serverUrl = getServerUrl(mapper);

            Date previousUpdate = mapper.getPreviousUpdate();

            if (previousUpdate != null) {
                String lastMod = dateUtils.formatForSitemap(previousUpdate);

                if (mapper instanceof PaginationSitemapper) {
                    PaginationSitemapper paginationMapper = (PaginationSitemapper) mapper;
                    int count = paginationMapper.getPagesCount();
                    for (int i = 0; i < count; i++) {
                        writeIndexExtry(writer, serverUrl, mapperName + "-" + i, lastMod);
                    }
                } else {
                    writeIndexExtry(writer, serverUrl, mapperName, lastMod);
                }
            } else {
                log.debug("No entries found for " + mapperName);
            }
        }

        writeIndexTail(writer);
    }

    @Override
    public void writeSitemapEntries(PrintWriter writer, Sitemapper sitemapper, Integer pageNumber) throws IOException {
        writeSitemapHead(writer);
        super.writeSitemapEntries(writer, sitemapper, pageNumber);
        writeSitemapTail(writer);
    }

    @Override
    public void writeSitemapEntries(PrintWriter writer, Sitemapper mapper) throws IOException {
        String serverUrl = getServerUrl(mapper);

        EntryWriter entryWriter = new XmlEntryWriter(writer, serverUrl);
        mapper.withEntryWriter(entryWriter);
    }

    private void writeIndexHead(PrintWriter writer) {
        writer.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        writer.print("\n");
        writer.print("<sitemapindex xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");
        writer.print("\n");
    }

    private void writeIndexTail(PrintWriter writer) {
        writer.print("</sitemapindex>");
    }

    private void writeIndexExtry(PrintWriter writer, String serverUrl, String mapperName, String lastMod) throws
            IOException {
        writer.print(SITEMAP_OPEN);
        writer.print(String.format("<loc>%s/%s.%s.%s</loc>", serverUrl, path, mapperName, extension));
        writer.print(String.format("<lastmod>%s</lastmod>", lastMod));
        writer.print(SITEMAP_CLOSE);
    }

    private void writeSitemapHead(PrintWriter writer) {
        writer.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        writer.print("\n");
        writer.print("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");
        writer.print("\n");
    }

    private void writeSitemapTail(PrintWriter writer) {
        writer.print("</urlset>");
    }

    private String getServerUrl(Sitemapper sitemapper) {
        String defaultServerUrl = serverUrlResolver.getServerUrl();

        // override server url for some sitemaps
        String serverUrl = defaultServerUrl;

        if (sitemapper instanceof SitemapServerUrlResolver &&
                ((SitemapServerUrlResolver) sitemapper).getServerUrl() != null) {
            serverUrl = ((SitemapServerUrlResolver) sitemapper).getServerUrl();
        }

        return serverUrl;
    }

}