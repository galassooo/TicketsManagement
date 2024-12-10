document.addEventListener("DOMContentLoaded", function () {
    // Get DOM elements
    const searchButton = document.getElementById("searchButton");
    const searchField = document.getElementById("searchField");
    const ticketsContainer = document.getElementById("ticketsContainer");
    const searchForm = document.querySelector('.search-container form'); // Get the search form

    // Verify if elements exist
    if (!searchButton || !searchField || !ticketsContainer) {
        console.error("Required elements not found");
        return;
    }

    // Prevent form submission
    if (searchForm) {
        searchForm.addEventListener('submit', function(e) {
            e.preventDefault();
        });
    }

    // Variable to store the timeout ID
    let searchTimeout;

    // Add event listener for input changes
    searchField.addEventListener("input", function() {
        // Clear any existing timeout
        if (searchTimeout) {
            clearTimeout(searchTimeout);
        }

        // Get the current value
        const searchQuery = this.value.trim();

        // If the length is 3 or more characters, set a timeout to search
        if (searchQuery.length >= 3) {
            searchTimeout = setTimeout(() => {
                handleSearch();
            }, 300); // Wait 300ms after the user stops typing
        } else if (searchQuery.length === 0) {
            // If the search field is empty, reload the page to show all tickets
            window.location.reload();
        }
    });

    // Add event listener to the button
    searchButton.addEventListener("click", function(e) {
        e.preventDefault(); // Prevent any default button behavior
        handleSearch();
    });

    // Add event listener for Enter key
    searchField.addEventListener("keypress", function(event) {
        if (event.key === "Enter") {
            event.preventDefault(); // Prevent form submission
            handleSearch();
        }
    });

    function handleSearch() {
        // Show loading state
        ticketsContainer.innerHTML = '<div class="loading">Searching...</div>';

        // Validate and get search query
        const searchQuery = searchField.value.trim();
        if (!searchQuery) {
            ticketsContainer.innerHTML = '<div class="error">Please enter a search term</div>';
            return;
        }

        // Send an AJAX request using Fetch API
        fetch(`/tickets/search?q=${encodeURIComponent(searchQuery)}`, {
            method: 'GET',
            headers: {
                'Accept': 'application/json'
            }
        })
            .then(response => {
                if (response.status === 204) { // No Content
                    throw new Error("NO_CONTENT");
                }
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(tickets => {
                // Clear the current ticket list
                ticketsContainer.innerHTML = "";

                // Create sections for different ticket states
                const sections = {
                    OPEN: createSection("Ticket Aperti", "open_tickets"),
                    CLOSED: createSection("Ticket Chiusi", "closed_tickets"),
                    IN_PROGRESS: createSection("Ticket In Lavorazione", "processing_tickets")
                };

                // Add tickets to appropriate sections
                tickets.forEach(ticket => {
                    const ticketElement = createTicketElement(ticket);
                    sections[ticket.status].querySelector('.content').appendChild(ticketElement);
                });

                // Add non-empty sections to container
                Object.values(sections).forEach(section => {
                    if (section.querySelector('.content').children.length > 0) {
                        ticketsContainer.appendChild(section);
                    }
                });

                if (ticketsContainer.children.length === 0) {
                    ticketsContainer.innerHTML = '<div class="no-results">No tickets found</div>';
                }
            })
            .catch(error => {
                if (error.message === "NO_CONTENT") {
                    ticketsContainer.innerHTML = '<div class="no-results">No tickets found</div>';
                } else {
                    ticketsContainer.innerHTML = `
                    <div class="error">
                        An error occurred while searching. Please try again later.
                    </div>
                `;
                    console.error('Search error:', error);
                }
            });
    }

    function createSection(title, id) {
        const section = document.createElement('section');
        section.id = id;
        section.innerHTML = `
            <h2>${title}</h2>
            <div class="content"></div>
        `;
        return section;
    }

    function createTicketElement(ticket) {
        const article = document.createElement('article');

        // Format date
        const date = new Date(ticket.date);
        const formattedDate = date.toLocaleDateString() + ' ' + date.toLocaleTimeString();

        article.innerHTML = `
            <h3>${ticket.title}</h3>
            <p><strong>Data:</strong> ${formattedDate}</p>
            <p><strong>Stato:</strong> ${ticket.status}</p>
            <p><strong>Tipo:</strong> ${ticket.type}</p>
            <p><strong>Utente:</strong> ${ticket.user.username}</p>
            <a href="/tickets/${ticket.id}" class="btn btn-primary">Info</a>
        `;

        return article;
    }
});