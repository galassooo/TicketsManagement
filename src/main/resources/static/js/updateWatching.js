document.addEventListener("DOMContentLoaded", function () {
    const buttonWatch = document.getElementById("watch-link");
    const WatchCounter = document.getElementById("watching");

    if (buttonWatch) {
        buttonWatch.addEventListener("click", function (e) {
            e.preventDefault();

            const ticketId = buttonWatch.href.split('/').slice(-2)[0];
            if (ticketId) {
                updateTicketDetails(ticketId);
            }
        });
    }

    function updateTicketDetails(ticketId) {
        WatchCounter.innerHTML = '<div class="loading">Loading details...</div>';

        // Prima fai la chiamata watch e ASPETTA che finisca
        fetch(`/tickets/${ticketId}/watch`)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                // Dopo che il watch è completato, fai la chiamata per le info
                return fetch(`/tickets/${ticketId}/info`, {
                    method: 'GET',
                    headers: {
                        'Accept': 'application/json'
                    }
                });
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(ticketDetails => {
                WatchCounter.innerHTML = `
                    <strong>Watching:</strong>
                    <em>${ticketDetails.watchers.length}</em>
                `;
                // Opzionalmente, nascondi il pulsante watch dopo il successo
                buttonWatch.style.display = 'none';
            })
            .catch(error => {
                console.error('Error details:', {
                    message: error.message,
                    stack: error.stack,
                    response: error.response,
                    error: error
                });
                WatchCounter.innerHTML = `
                    <div class="error">
                        An error occurred while loading ticket details. Please try again later.
                    </div>
                `;
            });
    }
});