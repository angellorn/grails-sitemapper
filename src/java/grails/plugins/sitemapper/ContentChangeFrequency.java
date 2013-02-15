package grails.plugins.sitemapper;

/**
 * How frequently the page is likely to change. This value provides general information to search
 * engines and may not correlate exactly to how often they crawl the page.
 * <p/>
 * The value "always" should be used to describe documents that change each time they are accessed.
 * The value "never" should be used to describe archived URLs.
 * <p/>
 * Please note that the value of this tag is considered a hint and not a command.
 * Even though search engine crawlers may consider this information when making decisions,
 * they may crawl pages marked "hourly" less frequently than that, and they may crawl pages
 * marked "yearly" more frequently than that. Crawlers may periodically crawl pages marked
 * "never" so that they can handle unexpected changes to those pages.
 * <p/>
 * - from sitemaps.org
 */
public enum ContentChangeFrequency {

  ALWAYS, HOURLY, DAILY, WEEKLY, MONTHLY, YEARLY, NEVER

}
