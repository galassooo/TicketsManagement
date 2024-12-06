document.addEventListener("DOMContentLoaded", function() {
    const form = document.querySelector('form');
    const existingSubmit = form.querySelector('input[type="submit"]');
    existingSubmit.remove();

    const fields = {
        name: {
            element: document.getElementById("name"),
            regex: /^[A-Za-z]+$/,
            valid: false
        },
        surname: {
            element: document.getElementById("surname"),
            regex: /^[A-Za-z]+$/,
            valid: false
        },
        username: {
            element: document.getElementById("username"),
            regex: /^[A-Za-z0-9_]+$/,
            valid: false
        },
        password: {
            element: document.getElementById("password"),
            regex: /^[A-Za-z0-9_]{8,15}$/,
            valid: false
        },
        confirmPassword: {
            element: document.getElementById("confirmPassword"),
            regex: /^[A-Za-z0-9_]{8,15}$/,
            valid: false,
            matchPassword: true
        }
    };

    function createSubmitButton() {
        const submitButton = document.createElement('input');
        submitButton.type = 'submit';
        submitButton.value = 'Register';
        return submitButton;
    }

    function checkAllFieldsValid() {
        return Object.values(fields).every(field => field.valid);
    }

    function updateSubmitButton() {
        const existingButton = form.querySelector('input[type="submit"]');

        if (checkAllFieldsValid()) {
            if (!existingButton) {
                form.appendChild(createSubmitButton());
            }
        } else if (existingButton) {
            existingButton.remove();
        }
    }

    function validatePasswords() {
        const passwordsMatch = fields.password.element.value === fields.confirmPassword.element.value;
        fields.confirmPassword.valid = passwordsMatch && fields.confirmPassword.regex.test(fields.confirmPassword.element.value);

        if (passwordsMatch) {
            fields.confirmPassword.element.classList.remove("error-message");
            fields.confirmPassword.element.classList.add("valid-message");
        } else {
            fields.confirmPassword.element.classList.remove("valid-message");
            fields.confirmPassword.element.classList.add("error-message");
        }

        updateSubmitButton();
    }

    function validate(input, regex, fieldKey) {
        const isValid = regex.test(input.value);
        fields[fieldKey].valid = isValid;

        if (isValid) {
            input.classList.remove("error-message");
            input.classList.add("valid-message");
        } else {
            input.classList.remove("valid-message");
            input.classList.add("error-message");
        }

        if (fieldKey === 'password' || fieldKey === 'confirmPassword') {
            validatePasswords();
        } else {
            updateSubmitButton();
        }
    }

    Object.entries(fields).forEach(([key, field]) => {
        field.element.addEventListener("input", () => {
            validate(field.element, field.regex, key);
        });
    });
});