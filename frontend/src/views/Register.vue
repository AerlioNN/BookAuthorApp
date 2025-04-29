<!-- eslint-disable vue/multi-word-component-names -->
<template>
  <div>
    <h2>Register</h2>

    <form @submit.prevent="handleRegister">
      <div>
        <label for="username">Username:</label>
        <input type="text" v-model="username" id="username" required />
      </div>

      <div>
        <label for="password">Password:</label>
        <input type="password" v-model="password" id="password" required />
      </div>

      <div>
        <button type="submit">Register</button>
      </div>
    </form>

    <p>
      Already have an account? <router-link to="/login">Login here</router-link>
    </p>
  </div>
</template>

<script>
export default {
  data() {
    return {
      username: "",
      password: "",
    };
  },
  methods: {
    async handleRegister() {
      try {
        const response = await fetch("/api/auth/register", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            username: this.username,
            password: this.password,
          }),
        });

        const result = await response.text();

        if (result === "User registered successfully") {
          alert("Registration successful! You can now log in.");
          this.$router.push("/login"); // Redirect to login page
        } else {
          alert("Registration failed: " + result);
        }
      } catch (error) {
        console.error("Registration failed", error);
        alert("An error occurred while registering.");
      }
    },
  },
};
</script>