document.addEventListener("DOMContentLoaded", function () {
    // Get reference to InfoBar container
    const infoBar = document.getElementById("InfoBar");

    // Add click event listeners to all tickets
    document.querySelectorAll("#grid article").forEach(article => {
        article.addEventListener("click", function () {
            const ticketId = this.id;
            if (ticketId) {
                updateTicketDetails(ticketId);
            }
        });
    });

    function updateTicketDetails(ticketId) {
        // Show loading indicator
        infoBar.innerHTML = '<div class="loading">Loading details...</div>';

        // Fetch ticket details
        fetch(`/tickets/${ticketId}/info`, {
            method: 'GET',
            headers: {
                'Accept': 'application/json'
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(ticketDetails => {
                // Render ticket details in InfoBar
                infoBar.innerHTML = `
                    <h2>Ticket Details</h2>
                    <p>Title: <strong>${ticketDetails.title}</strong></p>
                    <p>Type: <strong>${ticketDetails.type}</strong></p>
                    <p>Status: <strong>${ticketDetails.status}</strong></p>
                    <p>Estimate time: <strong>${ticketDetails.estimate}</strong></p>
                    <p>Spent time: <strong>${ticketDetails.timeSpent}</strong></p>
                    <p>Assigned to: <strong>${ticketDetails.assignee != null ? ticketDetails.assignee.username : 'Unassigned'}</strong></p>
                    <p>Description: <em>${ticketDetails.description}</em></p>
                `;
            })
            .catch(error => {
                console.error('Error fetching ticket details:', error);
                infoBar.innerHTML = `
                    <div class="error">
                        An error occurred while loading ticket details. Please try again later.
                    </div>
                `;
            });
    }
});
