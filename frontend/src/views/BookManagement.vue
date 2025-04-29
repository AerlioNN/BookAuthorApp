<template>
  <div class="books">
    <h1>Book Management</h1>

    <NavBar />

    <!-- Add Book -->
    <section>
      <h2>Add a New Book</h2>
      <form @submit.prevent="addBook">
        <label>Title:
          <input v-model="newBook.title" required />
        </label>
        <br />
        <label>Author ID:
          <input v-model.number="newBook.authorId" type="number" required />
        </label>
        <br />
        <button type="submit">Add Book</button>
      </form>
    </section>

    <!-- Update Book -->
    <section>
      <h2>Update a Book</h2>
      <form @submit.prevent="updateBook">
        <label>Book ID:
          <input v-model.number="updateData.id" type="number" required />
        </label>
        <br />
        <label>New Title:
          <input v-model="updateData.title" required />
        </label>
        <br />
        <label>New Author ID:
          <input v-model.number="updateData.authorId" type="number" required />
        </label>
        <br />
        <button type="submit">Update Book</button>
      </form>
    </section>

    <!-- Delete Book -->
    <section>
      <h2>Delete a Book</h2>
      <form @submit.prevent="deleteBook">
        <label>Book ID:
          <input v-model.number="deleteBookId" type="number" required />
        </label>
        <br />
        <button type="submit">Delete Book</button>
      </form>
    </section>

    <!-- Get Book with Author-->
    <section>
      <h2>Get Book with Author</h2>
      <form @submit.prevent="getBookWithAuthor">
        <label>Book ID:
          <input v-model.number="getBookId" type="number" required />
        </label>
        <button type="submit">Get Book</button>
      </form>
      <div v-if="bookDetails">
        <h3>Book Details</h3>
        <p><strong>Title:</strong> {{ bookDetails.title }}</p>
        <p><strong>Author:</strong> {{ bookDetails.authorName }}</p>
      </div>
    </section>

    <!-- Display All Books -->
    <section>
      <h2>All Books</h2>
      <button @click="fetchBooks">Display All Books</button>
      <ul>
        <li v-for="book in books" :key="book.id">
          {{ book.id }}: "{{ book.title }}" (Author ID: {{ book.authorId }})
        </li>
      </ul>
    </section>

    <div v-if="message">{{ message }}</div>
  </div>
</template>

<script>
import NavBar from "../components/NavBar.vue";

export default {
  name: 'BookManagement',
   components: {
      NavBar
   },
  data() {
    return {
      newBook: {
        title: '',
        authorId: null,
      },
      updateData: {
        id: null,
        title: '',
        authorId: null,
      },
      deleteBookId: null,
      getBookId: null,
      bookDetails: null,
      books: [],
      message: '',
    };
  },
  methods: {
    async addBook() {
      const response = await fetch('/books', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(this.newBook),
      });
      this.message = await response.text();
    },

    async updateBook() {
      const response = await fetch(`/books/${this.updateData.id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          title: this.updateData.title,
          authorId: this.updateData.authorId,
        }),
      });
      this.message = await response.text();
    },

    async deleteBook() {
      const response = await fetch(`/books/${this.deleteBookId}`, {
        method: 'DELETE',
      });
      this.message = await response.text();
    },

    async getBook() {
      const response = await fetch(`/books/${this.getBookId}`);
      if (response.ok) {
        this.bookDetails = await response.json();
      } else {
        this.message = await response.text();
        this.bookDetails = null;
      }
    },

    async fetchBooks() {
      const response = await fetch('/books');
      this.books = await response.json();
    },

    async getBookWithAuthor() {
      if (!this.getBookId) {
        this.message = "Please enter a valid Book ID.";
        this.bookDetails = null;
        return;
      }

      try {
        const response = await fetch(`/books/${this.getBookId}/with-author`);

        if (!response.ok) {
          this.message = await response.text();
          this.bookDetails = null;
        } else {
          this.bookDetails = await response.json();
          this.message = "";
        }
      } catch (error) {
        console.error("Error fetching book:", error);
        this.message = "Failed to fetch book. Please try again later.";
        this.bookDetails = null;
      }
    }
  },
};
</script>

<style scoped>
nav a {
  margin-right: 10px;
}
</style>
