using Microsoft.AspNetCore.Builder;
using Microsoft.Extensions.DependencyInjection;

namespace bekk.oppdrift.nugeteksempel.Extensions;

public static class ApplicationBuilderExtensions
{
    /// <summary>
    /// Legger til en middleware som vil svare på request
    /// dersom den inneholder query-parameteret "oppdrift".
    /// </summary>
    public static IApplicationBuilder UseOppdrift(this IApplicationBuilder app)
    {
        var factory = app.ApplicationServices.GetRequiredService<IOppdriftRequestHandlerFactory>();
        return app.Use(factory.MiddleWare());
    }
}