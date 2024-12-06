 document.addEventListener("DOMContentLoaded", () => {
                            // Select all forms with the class 'upvoteForm'
                            const forms = document.querySelectorAll(".upvoteForm");

                            forms.forEach((form) => {
                                form.addEventListener("submit", (event) => {
                                    // Get the reputation input fields within the current form
                                    const userReputation = parseInt(form.querySelector(".reputationInput").value, 10);
                                    const minReputation = parseInt(form.querySelector(".minReputationInput").value, 10);

                                    if (userReputation < minReputation || !user) {
                                        event.preventDefault(); // Prevent the form submission
                                        alert(`You need at least ${minReputation} reputation points to upvote!`);
                                    }
                                });
                            });
                        });