using Microsoft.AspNetCore.Http;

namespace bekk.oppdrift.nugeteksempel;

/// <summary>
/// En factory for å opprette oppdrift-response.
/// </summary>
public interface IOppdriftRequestHandlerFactory
{
    /// <summary>
    /// En <seealso cref="RequestDelegate"/> som returnerer en oppdrift-respons.
    /// </summary>
    RequestDelegate Handle { get; }

    Func<HttpContext, Func<Task>, Task> MiddleWare();
}