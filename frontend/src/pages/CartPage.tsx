import { useCart } from "../context/CartContext";

const krw = new Intl.NumberFormat("ko-KR");

export default function CartPage() {
  const { items, loading, error, setQuantity, remove, totalPrice } = useCart();

  if (loading && items.length === 0) return <div style={{ padding: 24 }}>장바구니 불러오는 중…</div>;
  if (error) return <div style={{ padding: 24, color: "crimson" }}>오류: {error}</div>;
  if (items.length === 0)
    return (
      <div style={{ padding: 24 }}>
        <h1 style={{ fontSize: 22, fontWeight: 700, marginBottom: 12 }}>장바구니</h1>
        <p>장바구니가 비어 있습니다.</p>
      </div>
    );

  return (
    <div style={{ padding: 24, maxWidth: 880, margin: "0 auto" }}>
      <h1 style={{ fontSize: 22, fontWeight: 700, marginBottom: 24 }}>장바구니</h1>
      <ul style={{ display: "grid", gap: 16, listStyle: "none", padding: 0 }}>
        {items.map((item) => (
          <li key={item.id} style={{ display: "flex", gap: 16, border: "1px solid #ddd", borderRadius: 16, padding: 16 }}>
            <div style={{ width: 96, height: 96, background: "#f3f3f3", borderRadius: 12, overflow: "hidden", flexShrink: 0 }}>
              {item.product.imageUrl ? (
                <img src={item.product.imageUrl} alt={item.product.name} style={{ width: "100%", height: "100%", objectFit: "cover" }} />
              ) : (
                <div style={{ fontSize: 12, color: "#999", display: "grid", placeItems: "center", height: "100%" }}>No Image</div>
              )}
            </div>

            <div style={{ flex: 1, minWidth: 0, display: "flex", flexDirection: "column", justifyContent: "space-between" }}>
              <div>
                <div style={{ fontSize: 18, fontWeight: 600, whiteSpace: "nowrap", overflow: "hidden", textOverflow: "ellipsis" }}>
                  {item.product.name}
                </div>
                <div style={{ marginTop: 6, color: "#666" }}>₩{krw.format(item.product.price)}</div>
              </div>

              <div style={{ marginTop: 12, display: "flex", alignItems: "center", gap: 12 }}>
                <div style={{ display: "inline-flex", alignItems: "center", border: "1px solid #ccc", borderRadius: 10, overflow: "hidden" }}>
                  <button
                    style={{ width: 36, height: 36, fontSize: 18 }}
                    onClick={() => setQuantity(item, item.quantity - 1)}
                    aria-label="수량 줄이기"
                  >
                    −
                  </button>
                  <input
                    type="number"
                    inputMode="numeric"
                    value={item.quantity}
                    onChange={(e) => {
                      const v = Math.max(0, Number(e.target.value || 0));
                      setQuantity(item, v);
                    }}
                    style={{ width: 56, height: 36, textAlign: "center", borderLeft: "1px solid #eee", borderRight: "1px solid #eee" }}
                  />
                  <button
                    style={{ width: 36, height: 36, fontSize: 18 }}
                    onClick={() => setQuantity(item, item.quantity + 1)}
                    aria-label="수량 늘리기"
                  >
                    +
                  </button>
                </div>

                <button
                  onClick={() => remove(item.id)}
                  style={{ fontSize: 14, color: "#666" }}
                >
                  삭제
                </button>

                <div style={{ marginLeft: "auto", fontWeight: 600 }}>
                  소계: ₩{krw.format(item.product.price * item.quantity)}
                </div>
              </div>
            </div>
          </li>
        ))}
      </ul>

      <div style={{ marginTop: 24, display: "flex", alignItems: "center", justifyContent: "flex-end", gap: 16 }}>
        <div style={{ fontSize: 20, fontWeight: 700 }}>총합: ₩{krw.format(totalPrice)}</div>
        <button style={{ padding: "10px 16px", borderRadius: 12, background: "#000", color: "#fff" }}>주문하기</button>
      </div>
    </div>
  );
}
