export default {
  host: "http://localhost:8080",
  recaptchaKey: "6Lcp1xAaAAAAAEp6YI3vE4rLG5Ehgj4EeMip04er",
  pattern: {
    username: /^[a-zA-Z]([-_a-zA-Z0-9]{5,19})+$/,
    email: /^\S+@\S+$/,
    password: /^.{6,20}$/,
    account: /^\S{1,20}$/
  }
}
