import axios from 'axios';

export default axios.create({
    baseURL: "http://localhost:8080",
    headers: {
        post: {
            Accept: 'application/json'
        },
        put: {
            Accept: 'application/json'
        }
    }
});