import { useEffect, useState } from "react";
import { fetchProducts, Product } from "../api/products";
import { Link } from "react-router-dom";
import AddToCartButton from "../components/AddToCartButton";

export default function Home() {
  const [items, setItems] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [err, setErr] = useState<string | null>(null);

  useEffect(() => {
    fetchProducts()
      .then(setItems)
      .catch((e) => setErr(e?.message ?? "Failed to load"))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div>Loading...</div>;
  if (err) return <div style={{ color: "crimson" }}>Error: {err}</div>;
  if (!items.length) return <div>상품이 없습니다.</div>;

  return (
    <div style={{ padding: 16 }}>
      {/* 상단 네비게이션: 장바구니로 이동 */}
      <nav style={{ marginBottom: 16, display: "flex", gap: 12 }}>
        <Link to="/">홈</Link>
        <Link to="/cart">장바구니</Link>
      </nav>

      <div
        style={{
          display: "grid",
          gridTemplateColumns: "repeat(auto-fill, minmax(220px, 1fr))",
          gap: 16,
        }}
      >
        {items.map((p) => (
          <div
            key={p.id}
            style={{
              border: "1px solid #eee",
              borderRadius: 12,
              padding: 16,
              boxShadow: "0 2px 10px rgba(0,0,0,.04)",
              display: "flex",
              flexDirection: "column",
              gap: 8,
            }}
          >
            {/* 제목만 링크로: 디테일 페이지 이동 */}
            <Link
              to={`/products/${p.id}`}
              style={{ textDecoration: "none", color: "inherit" }}
            >
              <h3 style={{ margin: 0, fontSize: 18 }}>{p.name}</h3>
            </Link>

            <div style={{ fontWeight: 700 }}>₩ {Number(p.price).toLocaleString()}</div>
            <small style={{ color: "#666" }}>재고: {p.stock}</small>

            <div style={{ marginTop: 8 }}>
              <AddToCartButton productId={p.id} />
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
