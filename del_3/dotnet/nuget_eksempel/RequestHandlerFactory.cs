using System.Text;
using Microsoft.AspNetCore.Http;

namespace bekk.oppdrift.nugeteksempel;
public class RequestHandlerFactory : IOppdriftRequestHandlerFactory
{
    public RequestDelegate Handle =>
        async ctx =>
    {
        ctx.Response.ContentType = "text/html; charset=UTF-8";
        ctx.Response.StatusCode = 200;
        await WriteAsync(ctx.Response, "<html><head><title>Hilsen fra oppdrift</title></head><body>");
        var rnd = new Random();
        var behaviours = new[] { "scroll", "slide", "alternate" };
        var directions = new[] { "left", "right", "up", "down" };
        
        for (int i = 0; i < 100; i++)
        {
            await WriteAsync(ctx.Response,
                $"<marquee behavior='{PickValue(behaviours, rnd)}' direction='{PickValue(directions, rnd)}' scrollamount='{rnd.Next(50)}' style='color: #{rnd.Next(16777216):x6}; font-size: {rnd.Next(60)}px;'>Oppdrift!</marquee>");
        }

        await WriteAsync(ctx.Response, "</body></html>");
    };
    
    private async Task WriteAsync(HttpResponse response, string content)
    {
        var bytes = Encoding.UTF8.GetBytes(content);
        await response.Body.WriteAsync(bytes);
    }

    private string PickValue(string[] values, Random rnd) => values[rnd.Next(values.Length - 1)];
}