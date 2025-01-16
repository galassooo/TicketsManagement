document.addEventListener("DOMContentLoaded", function () {
    // Get reference to InfoBar container
    const statusBox = document.getElementById("StatusBox");
    const link = document.getElementById("changeStatusLink");

    if (link) {
        link.addEventListener("click", function (e) {
            e.preventDefault();

            const ticketID = link.href.split('/').slice(-2)[0];
            if (ticketID) {
                updateMilestoneDetails(ticketID);
            }
        });
    }

    function updateMilestoneDetails(ticketID) {
        // Prima fai la chiamata watch e ASPETTA che finisca
        fetch(`/tickets/${ticketID}/changeStatus`)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(tckDetails => {
                statusBox.innerHTML = `
                    <strong>Stato:</strong>
                    <em>${tckDetails.status}</em>
                `;
            })
            .catch(error => {
                console.error('Error details:', {
                    message: error.message,
                    stack: error.stack,
                    response: error.response,
                    error: error
                });
                statusBox.innerHTML = `
                    <div class="error">
                        An error occurred while loading ticket details. Please try again later.
                    </div>
                `;
            });
    }
});