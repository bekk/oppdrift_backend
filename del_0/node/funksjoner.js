const writeOutBody = (request) => {
    let body = [];
    request
        .on('data', chunk => {
            body.push(chunk);
        })
        .on('end', () => {
            console.info(body.toString());
        });
}

//console.info(request.headers);

//response.setHeader("Set-Cookie", "innholdet i cookie");