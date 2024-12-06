   tinymce.init({
            selector: '#body', // The textarea ID
            plugins: 'image link media table paste codesample',
            toolbar: 'undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | ' +
                     'bullist numlist outdent indent | link image media codesample | removeformat',
            image_title: true,
            automatic_uploads: false, // Disable automatic uploads
            file_picker_types: 'image',
            // File picker callback for browsing files
            file_picker_callback: function (callback, value, meta) {
                // Create a file input element
                var input = document.createElement('input');
                input.setAttribute('type', 'file');
                input.setAttribute('accept', 'image/*');

                // Trigger the file input when clicked
                input.click();

                // Handle the file selection
                input.onchange = function () {
                    var file = input.files[0];
                    var reader = new FileReader();

                    reader.onload = function (e) {
                        // Send the file to the server using FormData
                        var formData = new FormData();
                        formData.append('file', file);

                        // Upload the image to the server
                        fetch('/uploads/image', {
                            method: 'POST',
                            body: formData
                        })
                        .then(response => response.json()) // Parse JSON response
                        .then(data => {
                            console.log(data); // Log the response for debugging
                            if (data.location) {
                                callback(data.location); // Provide TinyMCE with the image URL
                            } else {
                                alert('Image upload failed');
                            }
                        })
                        .catch(err => {
                            console.error('Error:', err); // Log the error
                            alert('Error: ' + err.message); // Notify user on failure
                        });
                    };
                    reader.readAsDataURL(file);
                };
            }
        });
