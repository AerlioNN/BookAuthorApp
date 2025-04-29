<template>
  <!-- Navigation -->
  <nav>
     <router-link to="/home">Home Page</router-link> |
     <router-link to="/authors">Manage Authors</router-link> |
     <router-link to="/books">Manage Books</router-link>
     <button @click="logout">Logout</button>
  </nav>
</template>

<script>
export default {
  methods: {
    logout() {
      localStorage.removeItem("token");
      this.$router.push("/login");
    }
  }
};
</script>
