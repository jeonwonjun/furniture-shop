import { useEffect, useState } from "react";
import { fetchProducts, Product } from "../api/products";
import { Link } from "react-router-dom";

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
    <div
      style={{
        display: "grid",
        gridTemplateColumns: "repeat(auto-fill, minmax(220px, 1fr))",
        gap: 16,
      }}
    >
      {items.map((p) => (
        <Link
          key={p.id}
          to={`/products/${p.id}`}
          style={{ textDecoration: "none", color: "inherit" }}
        >
          <div
            style={{
              border: "1px solid #eee",
              borderRadius: 12,
              padding: 16,
              boxShadow: "0 2px 10px rgba(0,0,0,.04)",
            }}
          >
            <h3 style={{ margin: "0 0 8px", fontSize: 18 }}>{p.name}</h3>
            <div style={{ fontWeight: 700, marginBottom: 6 }}>
              ₩ {Number(p.price).toLocaleString()}
            </div>
            <small style={{ color: "#666" }}>재고: {p.stock}</small>
          </div>
        </Link>
      ))}
    </div>
  );
}
