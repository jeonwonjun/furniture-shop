import { useState } from "react";
import { useCart } from "../context/CartContext";

export default function AddToCartButton({ productId, defaultQty = 1 }) {
  const { add, loading } = useCart();
  const [pending, setPending] = useState(false);

  const handleClick = async () => {
    try {
      setPending(true);
      await add(productId, defaultQty);
    } finally {
      setPending(false);
    }
  };

  return (
    <button
      onClick={handleClick}
      disabled={loading || pending}
      style={{
        padding: "8px 14px",
        borderRadius: 12,
        background: "#000",
        color: "#fff",
        cursor: loading || pending ? "not-allowed" : "pointer",
        opacity: loading || pending ? 0.6 : 1,
      }}
      aria-busy={loading || pending}
    >
      {pending ? "담는 중…" : "장바구니 담기"}
    </button>
  );
}
