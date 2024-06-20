const stripe = Stripe(
  'pk_test_51PTDlUBOR0GEtAtTCOr1ENwmE6FW7pocZ5qpMGtoWkqKInkRC2aXv3FNW69qMWCvBE5bVskgEPIupF6ySCeQh3QH00WhelTdQ0'
);
const paymentButton = document.querySelector('#paymentButton');

paymentButton.addEventListener('click', () => {
  stripe.redirectToCheckout({
    sessionId: sessionId
  })
});