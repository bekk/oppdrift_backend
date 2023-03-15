using System.Text;
using Microsoft.AspNetCore.Http;

namespace bekk.oppdrift.nugeteksempel;
public class RequestHandlerFactory : IOppdriftRequestHandlerFactory
{
    private readonly Random rnd = new();
    public RequestDelegate Handle =>
        async ctx =>
    {
        ctx.Response.ContentType = "text/html; charset=UTF-8";
        ctx.Response.StatusCode = 200;
        await WriteAsync(ctx.Response, "<html><head><title>Hilsen fra oppdrift</title></head><body style='background-image: url(\"https://raw.github.com/bekk/oppdrift_backend/main/img/Kodesnorkling.jpg\");'>");
        var behaviours = new[] { "scroll", "slide", "alternate" };
        var directions = new[] { "left", "right", "up", "down" };
        
        for (int i = 0; i < 400; i++)
        {
            await WriteAsync(ctx.Response,
                $"<marquee behavior='{PickValue(behaviours, rnd)}' direction='{PickValue(directions, rnd)}' scrollamount='{rnd.Next(50)}' style='color: #{rnd.Next(16777216):x6}; font-size: {rnd.Next(80)}px;'>Oppdrift!</marquee>");
        }

        await WriteAsync(ctx.Response, "</body></html>");
    };

    public Func<HttpContext, Func<Task>, Task> MiddleWare()
    {
        return async (context, next) =>
        {
            var query = context.Request.Query;
            if (query.TryGetValue("oppdrift", out var value))
            {
                var content = Encoding.UTF8.GetBytes($"Oppdrift svarer: {value}");
                var response = context.Response;
                response.StatusCode = 200;
                response.Cookies.Append("Oppdrift", value);
                response.ContentType = "text/plain; charset=UTF-8";
                await response.Body.WriteAsync(content);
            }
            else
            {
                await next();
            }
        };
    }

    private async Task WriteAsync(HttpResponse response, string content)
    {
        var bytes = Encoding.UTF8.GetBytes(content);
        await response.Body.WriteAsync(bytes);
    }

    private string PickValue(string[] values, Random rnd) => values[rnd.Next(values.Length - 1)];
}