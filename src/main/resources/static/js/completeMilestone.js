document.addEventListener("DOMContentLoaded", function () {
        // Get reference to InfoBar container
        const completeContainer = document.getElementById("complete_container");
        const link = document.getElementById("complete_link");
        const completedFlag = document.getElementById("completedFlag");

        if (link) {
            link.addEventListener("click", function (e) {
                e.preventDefault();

                const milestoneID = link.href.split('/').slice(-2)[0];
                if (milestoneID) {
                    updateMilestoneDetails(milestoneID);
                }
            });
        }

        function updateMilestoneDetails(milestoneID) {
            completedFlag.innerHTML = '<div class="loading">Loading details...</div>';

            // Prima fai la chiamata watch e ASPETTA che finisca
            fetch(`/tickets/milestones/${milestoneID}/complete`)
                .then(response => {
                    if (!response.ok) {
                        throw new Error(`HTTP error! status: ${response.status}`);
                    }
                    return response.json();
                })
                .then(milestoneDetails => {
                    completedFlag.innerHTML = `
                    <strong>Stato:</strong>
                    <em>completed</em>
                `;
                    completeContainer.innerHTML = "";
                })
                .catch(error => {
                    console.error('Error details:', {
                        message: error.message,
                        stack: error.stack,
                        response: error.response,
                        error: error
                    });
                    completedFlag.innerHTML = `
                    <div class="error">
                        An error occurred while loading ticket details. Please try again later.
                    </div>
                `;
                });
        }
    });