 document.addEventListener("DOMContentLoaded", () => {
                            // Select all forms with the class 'upvoteForm'
                            const forms = document.querySelectorAll(".upvoteForm");

                            forms.forEach((form) => {
                                form.addEventListener("submit", (event) => {
                                    // Get the reputation input fields within the current form
                                    const userReputation = parseInt(form.querySelector(".reputationInput").value, 10);
                                    const minReputation = parseInt(form.querySelector(".minReputationInput").value, 10);
                                    const username = form.querySelector(".username").value;
                                    const authorname = form.querySelector(".authorname").value;
                                  if (
                                      username === authorname ||
                                      userReputation === 0 ||
                                      userReputation < minReputation
                                  ) {
                                      event.preventDefault(); // Prevent the form submission
                                      const message =
                                          username === authorname
                                              ? "You can't perform this action on your own data..."
                                              : userReputation === 0
                                                  ? "You need to login first..."
                                                  : `You need at least ${minReputation} reputation points...`;
                                      alert(message);
                                  }


                                });
                            });
                        });