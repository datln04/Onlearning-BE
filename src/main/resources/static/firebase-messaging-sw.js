importScripts('https://www.gstatic.com/firebasejs/9.1.1/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/9.1.1/firebase-messaging-compat.js');

const firebaseConfig = {
    // Your Firebase project configuration goes here
    apiKey: "AIzaSyBdNnOg_OjNkKsxd3QEAvqDjvgeYWtQUIg",
    authDomain: "fir-fcm-8d942.firebaseapp.com",
    projectId: "fir-fcm-8d942",
    storageBucket: "fir-fcm-8d942.appspot.com",
    messagingSenderId: "884651803428",
    appId: "1:884651803428:web:0c00ae7605494184ffe9b1",
    measurementId: "G-FJQ0176Y0M",
};

firebase.initializeApp(firebaseConfig);

const messaging = firebase.messaging();

messaging.onBackgroundMessage((payload) => {
    console.log('Received background message:', payload);
    // Customize notification here
    const notificationTitle = 'Background Message Title';
    const notificationOptions = {
        body: 'Background Message body.',
        icon: '/firebase-logo.png'
    };

    self.registration.showNotification(notificationTitle, notificationOptions);
});