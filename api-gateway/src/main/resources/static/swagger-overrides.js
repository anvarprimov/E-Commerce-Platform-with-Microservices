// Runs after Swagger UI loads
(function () {
    // Helper: hide all children of the form except the button row
    function trimOauthModal() {
        var form = document.querySelector('.modal-ux .auth-container form');
        if (!form) return false;

        // Hide headings/help text
        ['h2','.markdown','p','.description','.info','.title','.message','.helper']
            .forEach(sel => document.querySelectorAll('.modal-ux .auth-container ' + sel)
                .forEach(el => el.style.display = 'none'));

        // Hide inputs/labels/parameters/scopes
        ['.parameters','.parameters-container','.wrapper','.metadata','.prop','label',
            'input[type="text"]','input[type="password"]','.field','.parameter__name',
            '.parameter__value','.scopes-wrapper','.scopes','.scope-definitions',
            '.scope-name','.scope-description','.select-all']
            .forEach(sel => form.querySelectorAll(sel).forEach(el => el.style.display = 'none'));

        // Keep only the button row
        var btnRow = form.querySelector('.auth-btn-wrapper');
        if (btnRow) {
            btnRow.style.display = 'flex';
            btnRow.style.justifyContent = 'flex-end';
            btnRow.style.gap = '8px';
        }

        // Shrink modal height
        var modal = document.querySelector('.modal-ux .modal');
        if (modal) modal.style.maxHeight = '240px';

        return true;
    }

    // Observe DOM; whenever the modal appears, trim it
    var obs = new MutationObserver(() => { trimOauthModal(); });
    obs.observe(document.body, { childList: true, subtree: true });

    // Also try once after load in case modal is already open
    setTimeout(trimOauthModal, 200);
})();
