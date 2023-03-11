using Microsoft.Extensions.DependencyInjection;

namespace bekk.oppdrift.nugeteksempel.Extensions;

public static class ServiceCollectionExtensions
{
    /// <summary>
    /// Legger til tjenesten <seealso cref="IOppdriftRequestHandlerFactory"/>.
    /// </summary>
    public static IServiceCollection AddOppdrift(this IServiceCollection services)
    {
        services.AddSingleton<IOppdriftRequestHandlerFactory, RequestHandlerFactory>();
        return services;
    }
}