<!-- eslint-disable vue/multi-word-component-names -->
<template>
  <div>
    <h2>Login</h2>

    <form @submit.prevent="handleLogin">
      <div>
        <label for="username">Username:</label>
        <input type="text" v-model="username" id="username" required />
      </div>

      <div>
        <label for="password">Password:</label>
        <input type="password" v-model="password" id="password" required />
      </div>

      <div>
        <button type="submit">Login</button>
      </div>
    </form>

    <p>
      Don't have an account yet? <router-link to="/register">Create one</router-link>
    </p>
  </div>
</template>

<script>
export default {
  name: "LoginPage", // <- DODANE
  data() {
    return {
      username: "",
      password: "",
    };
  },
  methods: {
    async handleLogin() {
      try {
        const response = await fetch("/api/auth/login", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            username: this.username,
            password: this.password,
          }),
        });

        const body = await response.text();

        if (!response.ok) {
          alert("Login failed: " + body);
          return;
        }

        // Save token and redirect
        localStorage.setItem("token", body);
        this.$router.push("/home");
      } catch (error) {
        console.error("Login failed", error);
        alert("An error occurred while logging in.");
      }
    },
  },
};
</script>
