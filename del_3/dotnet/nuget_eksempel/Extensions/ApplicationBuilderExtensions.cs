using System.Text;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Http;

namespace bekk.oppdrift.nugeteksempel.Extensions;

public static class ApplicationBuilderExtensions
{
    /// <summary>
    /// Legger til en middleware som vil svare på request
    /// dersom den inneholder query-parameteret "oppdrift".
    /// </summary>
    public static IApplicationBuilder UseOppdrift(this IApplicationBuilder app) =>
        app.Use(async (HttpContext context, Func<Task> next) =>
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
        });
}