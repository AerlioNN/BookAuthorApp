    // Add Book
    document.getElementById('addBookForm').addEventListener('submit', async function (event) {
        event.preventDefault();

        const title = document.getElementById('title').value;
        const authorId = document.getElementById('authorId').value;

        try {
            const response = await fetch('/books', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ title, authorId })
            });

            if (response.ok) {
                document.getElementById('result').innerHTML = '<p style="color: green;">Book added successfully!</p>';
            } else {
                throw new Error(`Error: ${response.status} ${response.statusText}`);
            }
        } catch (error) {
            document.getElementById('result').innerHTML = `<p style="color: red;">Failed to add book. ${error.message}</p>`;
        }
    });

    // Update Book
    document.getElementById('updateBookForm').addEventListener('submit', async function (event) {
        event.preventDefault();

        const bookId = document.getElementById('bookId').value;
        const newTitle = document.getElementById('newTitle').value;
        const newAuthorId = document.getElementById('newAuthorId').value;

        try {
            const response = await fetch(`/books/${bookId}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ title: newTitle, authorId: newAuthorId })
            });

            if (response.ok) {
                document.getElementById('result').innerHTML = '<p style="color: green;">Book updated successfully!</p>';
            } else {
                throw new Error(`Error: ${response.status} ${response.statusText}`);
            }
        } catch (error) {
            document.getElementById('result').innerHTML = `<p style="color: red;">Failed to update book. ${error.message}</p>`;
        }
    });

    // Delete Book
    document.getElementById('deleteBookForm').addEventListener('submit', async function (event) {
        event.preventDefault();

        const bookId = document.getElementById('deleteBookId').value;

        try {
            const response = await fetch(`/books/${bookId}`, { method: 'DELETE' });

            if (response.ok) {
                document.getElementById('result').innerHTML = '<p style="color: green;">Book deleted successfully!</p>';
            } else {
                throw new Error(`Error: ${response.status} ${response.statusText}`);
            }
        } catch (error) {
            document.getElementById('result').innerHTML = `<p style="color: red;">Failed to delete book. ${error.message}</p>`;
        }
    });

    // Get Book with Author
    document.getElementById('getBookForm').addEventListener('submit', async function (event) {
        event.preventDefault();

        const bookId = document.getElementById('getBookId').value;

        if (bookId) {
            try {
                const response = await fetch(`/books/${bookId}/with-author`);

                if (response.ok) {
                    const data = await response.json();

                    document.getElementById('result').innerHTML = `
                        <h3>Book Details</h3>
                        <p>Title: ${data.title}</p>
                        <p>Author: ${data.authorName}</p>
                    `;
                } else {
                    throw new Error(`Error: ${response.status} ${response.statusText}`);
                }
            } catch (error) {
                document.getElementById('result').innerHTML = `<p style="color: red;">Failed to retrieve book. ${error.message}</p>`;
            }
        } else {
            document.getElementById('result').innerHTML = `<p style="color: red;">Please provide a valid Book ID.</p>`;
        }
    });

    // Display All Books
document.getElementById('displayBooksButton').addEventListener('click', async function () {
    try {
        // Fetch all books
        const booksResponse = await fetch('/books');
        // Fetch all authors to map their IDs to names
        const authorsResponse = await fetch('/authors');

        if (booksResponse.ok && authorsResponse.ok) {
            const books = await booksResponse.json();
            const authors = await authorsResponse.json();

            // Create a map of authorId to authorName
            const authorMap = authors.reduce((map, author) => {
                map[author.id] = author.name;
                return map;
            }, {});

            const bookList = document.getElementById('bookList');
            bookList.innerHTML = ''; // Clear any existing list

            books.forEach(book => {
                const listItem = document.createElement('li');
                listItem.textContent = `Title: ${book.title}, Author: ${authorMap[book.authorId] || 'Unknown Author'}`;
                bookList.appendChild(listItem);
            });
        } else {
            throw new Error(`Error: ${booksResponse.statusText} or ${authorsResponse.statusText}`);
        }
    } catch (error) {
        document.getElementById('result').innerHTML = `<p style="color: red;">Failed to retrieve books or authors. ${error.message}</p>`;
    }
});