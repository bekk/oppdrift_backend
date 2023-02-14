using System.Net;
using System.Net.WebSockets;
using System.Text;
using Microsoft.Extensions.FileProviders;

var sockets = new List<(IPAddress? Ip, WebSocket Socket)>(); 

var builder = WebApplication.CreateBuilder(args);
var app = builder.Build();
app.UseWebSockets();

Console.WriteLine(Path.Combine(builder.Environment.ContentRootPath, "../../static"));

app.UseStaticFiles(new StaticFileOptions
{
    FileProvider = new PhysicalFileProvider(
        Path.Combine(builder.Environment.ContentRootPath, "../../static")),
    RequestPath = ""
});

app.MapGet("/api/{msg}", (string msg) => SendToAll(msg));

app.MapFallback("", async ctx =>
{
    if (ctx.WebSockets.IsWebSocketRequest)
    {
        using var webSocket = await ctx.WebSockets.AcceptWebSocketAsync();
        var sender = ctx.Connection.RemoteIpAddress;
        Console.WriteLine(sender);
        sockets.Add((sender, webSocket));

        {
            await webSocket.SendAsync("Welcome to the chat!"u8.ToArray(), WebSocketMessageType.Text,
                WebSocketMessageFlags.EndOfMessage, CancellationToken.None);

            var buffer = new byte[1024 * 4];
            var receiveResult = await webSocket.ReceiveAsync(new ArraySegment<byte>(buffer), CancellationToken.None);
            while (!receiveResult.CloseStatus.HasValue)
            {
                var msg = Encoding.UTF8.GetString(buffer);
                await SendToAll(msg);
                Array.Clear(buffer);
                receiveResult = await webSocket.ReceiveAsync(new ArraySegment<byte>(buffer), CancellationToken.None);
            }
        }
    }
});

app.Run();

async Task SendToAll(string msg)
{
    foreach (var (ip, socket) in sockets.Where(s => s.Socket.State == WebSocketState.Open))
    {
        await socket.SendAsync(Encoding.UTF8.GetBytes($"{ip}: {msg}"), WebSocketMessageType.Text, WebSocketMessageFlags.EndOfMessage, CancellationToken.None);
    }
}