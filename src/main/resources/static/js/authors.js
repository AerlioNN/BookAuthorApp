    // Add Author
    document.getElementById('addAuthorForm').addEventListener('submit', async function (event) {
        event.preventDefault();

        const name = document.getElementById('authorName').value;

        try {
            const response = await fetch('/authors', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ name })
            });

            if (response.ok) {
                document.getElementById('result').innerHTML = '<p style="color: green;">Author added successfully!</p>';
            } else {
                throw new Error(`Error: ${response.statusText}`);
            }
        } catch (error) {
            document.getElementById('result').innerHTML = `<p style="color: red;">Failed to add author. ${error.message}</p>`;
        }
    });

    // Update Author
    document.getElementById('updateAuthorForm').addEventListener('submit', async function (event) {
        event.preventDefault();

        const id = document.getElementById('updateAuthorId').value;
        const name = document.getElementById('updateAuthorName').value;

        try {
            const response = await fetch(`/authors/${id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ name })
            });

            if (response.ok) {
                document.getElementById('result').innerHTML = '<p style="color: green;">Author updated successfully!</p>';
            } else {
                throw new Error(`Error: ${response.statusText}`);
            }
        } catch (error) {
            document.getElementById('result').innerHTML = `<p style="color: red;">Failed to update author. ${error.message}</p>`;
        }
    });

    // Delete Author
    document.getElementById('deleteAuthorForm').addEventListener('submit', async function (event) {
        event.preventDefault();

        const id = document.getElementById('deleteAuthorId').value;

        try {
            const response = await fetch(`/authors/${id}`, { method: 'DELETE' });

            if (response.ok) {
                document.getElementById('result').innerHTML = '<p style="color: green;">Author deleted successfully!</p>';
            } else {
                throw new Error(`Error: ${response.statusText}`);
            }
        } catch (error) {
            document.getElementById('result').innerHTML = `<p style="color: red;">Failed to delete author. ${error.message}</p>`;
        }
    });

   // Get Author with Books
document.getElementById('getAuthorWithBooksForm').addEventListener('submit', async function (event) {
    event.preventDefault();

    const id = document.getElementById('getAuthorId').value;

    try {
        const response = await fetch(`/authors/${id}/with-books`);

        if (response.ok) {
            const data = await response.json();

            let resultHtml = `
                <h3>Author Details</h3>
                <p>Name: ${data.author.name}</p>
            `;

            // display books list if there are books bound to an author
            if (data.books && data.books.length > 0) {
                resultHtml += `
                    <h4>Books:</h4>
                    <ul>
                        ${data.books.map(book => `<li>${book.title}</li>`).join('')}
                    </ul>
                `;
            }

            // Display the result
            document.getElementById('result').innerHTML = resultHtml;
        } else {
            throw new Error(`Error: ${response.statusText}`);
        }
    } catch (error) {
        document.getElementById('result').innerHTML = `<p style="color: red;">Failed to fetch author. ${error.message}</p>`;
    }
});



    // Display All Authors
    document.getElementById('displayAuthorsButton').addEventListener('click', async function () {
        try {
            const response = await fetch('/authors');

            if (response.ok) {
                const authors = await response.json();
                const authorList = document.getElementById('authorList');
                authorList.innerHTML = '';

                authors.forEach(author => {
                    const listItem = document.createElement('li');
                    listItem.textContent = `Name: ${author.name}`;
                    authorList.appendChild(listItem);
                });
            } else {
                throw new Error(`Error: ${response.statusText}`);
            }
        } catch (error) {
            document.getElementById('result').innerHTML = `<p style="color: red;">Failed to fetch authors. ${error.message}</p>`;
        }
    });