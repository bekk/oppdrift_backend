using OpenQA.Selenium;
using OpenQA.Selenium.Chrome;
using OpenQA.Selenium.Firefox;
using OpenQA.Selenium.Support.UI;
using Xunit;
using Xunit.Abstractions;

namespace Bekk.Oppdrift.SeleniumExample;

public class WebDriverTests(ITestOutputHelper @out)
{
    [Fact]
    public void TestBekkWebPage_UsingChrome()
    {
        using var driver = new ChromeDriver();
        driver.Navigate().GoToUrl("https://www.bekk.no");
        var paragraphs = driver.FindElements(By.CssSelector("p"));
        foreach (var paragraph in paragraphs)
        {
            @out.WriteLine(paragraph.Text.Trim());
        }
        driver.Quit();
    }

    [Fact]
    public void SearchWikipedia_UsingFirefox()
    {
        using var driver = new FirefoxDriver();
        driver.Navigate().GoToUrl("https://www.wikipedia.org/");
        var langSelector = new SelectElement(driver.FindElement(By.Id("searchLanguage")));
        langSelector.SelectByValue("no");
        var query = driver.FindElement(By.Id("searchInput"));
        query.SendKeys("Bekk Consulting");
        query.Submit();
        var content = new WebDriverWait(driver, TimeSpan.FromSeconds(2))
            .Until(d => d.FindElement(By.Id("bodyContent")));
        foreach (var txt in content.FindElements(By.TagName("p")).Select(p => p.Text))
        {
            @out.WriteLine(txt);
        }
    }
}