<template>
  <div>
    <h1>Author Management</h1>

    <NavBar />

    <!-- Add Author -->
    <h2>Add a New Author</h2>
    <form @submit.prevent="addAuthor">
      <label for="authorName">Name:</label>
      <input type="text" v-model="authorName" required />
      <br />
      <button type="submit">Add Author</button>
    </form>

    <!-- Update Author -->
    <h2>Update an Author</h2>
    <form @submit.prevent="updateAuthor">
      <label for="updateAuthorId">Author ID:</label>
      <input type="number" v-model="updateAuthorId" required />
      <br />
      <label for="updateAuthorName">New Name:</label>
      <input type="text" v-model="updateAuthorName" required />
      <br />
      <button type="submit">Update Author</button>
    </form>

    <!-- Delete Author -->
    <h2>Delete an Author</h2>
    <form @submit.prevent="deleteAuthor">
      <label for="deleteAuthorId">Author ID:</label>
      <input type="number" v-model="deleteAuthorId" required />
      <br />
      <button type="submit">Delete Author</button>
    </form>

    <!-- Get Author Details -->
    <h2>Get Author with Books</h2>
    <form @submit.prevent="getAuthorDetails">
      <label for="getAuthorId">Author ID:</label>
      <input type="number" v-model="getAuthorId" required />
      <br />
      <button type="submit">Get Author Details</button>
    </form>

    <!-- Display All Authors -->
    <h2>All Authors</h2>
    <button @click="displayAllAuthors">Display All Authors</button>
    <ul>
      <li v-for="author in authors" :key="author.id">
        {{ author.name }} (ID: {{ author.id }})
      </li>
    </ul>

    <div id="result">{{ result }}</div>
  </div>
</template>

<script>
import NavBar from "../components/NavBar.vue";

export default {
  name: "AuthorManagement",
  components: {
      NavBar
  },
  data() {
    return {
      authorName: "",
      updateAuthorId: "",
      updateAuthorName: "",
      deleteAuthorId: "",
      getAuthorId: "",
      authors: [],
      result: "",
    };
  },
  methods: {
    async addAuthor() {
      await fetch("/authors", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ name: this.authorName }),
      });
      this.authorName = "";
    },
    async updateAuthor() {
      await fetch(`/authors/${this.updateAuthorId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ name: this.updateAuthorName }),
      });
      this.updateAuthorId = "";
      this.updateAuthorName = "";
    },
    async deleteAuthor() {
      await fetch(`/authors/${this.deleteAuthorId}`, {
        method: "DELETE",
      });
      this.deleteAuthorId = "";
    },
    async getAuthorDetails() {
      const res = await fetch(`/authors/${this.getAuthorId}`);
      const data = await res.json();
      this.result = JSON.stringify(data, null, 2);
    },
    async displayAllAuthors() {
      const res = await fetch("/authors");
      this.authors = await res.json();
    },
  },
};
</script>
