self.addEventListener('push', event => {
    const msg = event.data.json();
    event.waitUntil(self.registration.showNotification(
        `Message from ${msg.sender}`, {
            body: msg.msg,
            icon: '/notification-icon.png'
        }));
});