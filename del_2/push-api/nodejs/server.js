const express = require('express');
const webpush = require('web-push');

//const vapidKeys = webpush.generateVAPIDKeys();

const vapidKeys = {
    publicKey: "BF0qlcGT__LhAoJXe8KGfbQ-HLMmaBjHi81uJfHX1BMjE_GtbLAr9llKtKOKelcQmyjKHTTCMHewkIPNydf7Zrs",
    privateKey: "0n1z75VQLzVXv8avWP1SJg57rz7HebzoeCKp73Pkfe0"
};

webpush.setVapidDetails(
    'mailto:test@bekk.no',
    vapidKeys.publicKey,
    vapidKeys.privateKey);

const app = express();

app.use(express.static('static'));

app.use(express.json());

app.post('/api/subscription', (req, res) => {
    subscriptions.push(req.body);
    res.setHeader('Content-Type', 'application/json');
    res.send(JSON.stringify({ data: { success: true } }));
});

app.post('/api/broadcast', (req, res) => {
    const msg = JSON.stringify(req.body);
    subscriptions.forEach(subscription => {
        webpush.sendNotification(subscription, msg)
            .catch(err => {
                console.error(err);
            })
    });
    res.send('ok');
});

app.get('/api/publicKey', (req, res) => {
    res.send(vapidKeys.publicKey);
})

console.info("Listening to http://localhost:8080/");

app.listen(8080);

const subscriptions = [];