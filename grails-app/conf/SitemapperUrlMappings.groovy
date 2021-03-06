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
import grails.util.Holders
import org.springframework.context.ApplicationContext

/**
 * @author <a href='mailto:kim@developer-b.com'>Kim A. Betti</a>
 * @author <a href='mailto:donbeave@gmail.com'>Alexey Zhokhov</a>
 */
class SitemapperUrlMappings {

    static mappings = { ApplicationContext context ->
        def path = Holders.config.sitemap.prefix
        String extension = context.getBean('sitemapWriter').extension

        "/${path}.${extension}"(controller: 'sitemapper', plugin: 'sitemapper')
        "/${path}.${name}.${extension}"(controller: 'sitemapper', plugin: 'sitemapper', action: 'source')
    }

}
