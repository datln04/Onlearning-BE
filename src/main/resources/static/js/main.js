

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
messaging.onMessage((payload) => {
    console.log('Message received. ', payload);
    // handle message here
});

let FcmToken = '';
messaging.requestPermission().then(() => {
    console.log('Notification permission granted.');
    return messaging.getToken();
}).then((token) => {
    console.log('FCM token:', token);
    // send token to server
    FcmToken = token;


}).catch((err) => {
    console.log('Unable to get permission to notify.', err);
});

const app = Vue.createApp({
    data() {
        return {
            title: '',
            message: '',
            token: FcmToken,
        };
    },
    methods: {
        sendNotification() {
            fetch('http://localhost:8080/send-notification', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    token: FcmToken,
                    title: this.title,
                    message: this.message
                })
            }).then(response => {
                console.log('Notification sent successfully');
                handleResponse(response);
            })
                .catch(error => {
                    console.error('Error sending notification:', error);
                });
        }
    }
})

app.mount('#app');

function handleResponse(response) {
    console.log('Received response:', response);
    // handle response here
}