/**
 *
 */


const firebaseConfig = {
    apiKey: "AIzaSyBdNnOg_OjNkKsxd3QEAvqDjvgeYWtQUIg",
    authDomain: "fir-fcm-8d942.firebaseapp.com",
    projectId: "fir-fcm-8d942",
    storageBucket: "fir-fcm-8d942.appspot.com",
    messagingSenderId: "884651803428",
    appId: "1:884651803428:web:0c00ae7605494184ffe9b1",
    measurementId: "G-FJQ0176Y0M",
};

console.log("config: " + firebaseConfig);

firebase.initializeApp(firebaseConfig);

const messaging = firebase.messaging();


// Register the service worker and listen for incoming messages
navigator.serviceWorker.register('/firebase-messaging-sw.js')
    .then((registration) => {
        messaging.useServiceWorker(registration);

        // Handle incoming messages
        messaging.onMessage((payload) => {
            console.log('Received message:', payload);

            // Display notification to the user
            const notificationTitle = payload.notification.title;
            const notificationOptions = {
                body: payload.notification.body,
                icon: payload.notification.icon
            };
            registration.showNotification(notificationTitle, notificationOptions);
        });
    })
    .catch((err) => {
        console.error('Error registering service worker:', err);
    });