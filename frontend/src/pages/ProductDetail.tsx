import { useEffect, useState } from "react";
import { fetchProduct, Product } from "../api/products";
import { useParams, Link } from "react-router-dom";

export default function ProductDetail() {
  const { id } = useParams();
  const [product, setProduct] = useState<Product | null>(null);
  const [loading, setLoading] = useState(true);
  const [err, setErr] = useState<string | null>(null);

  useEffect(() => {
    if (!id) return;
    fetchProduct(Number(id))
      .then(setProduct)
      .catch((e) => setErr(e?.message ?? "Failed to load"))
      .finally(() => setLoading(false));
  }, [id]);

  if (loading) return <div>Loading...</div>;
  if (err) return <div style={{ color: "crimson" }}>Error: {err}</div>;
  if (!product) return <div>상품을 찾을 수 없습니다.</div>;

  return (
    <div style={{ maxWidth: 680 }}>
      <Link to="/" style={{ display: "inline-block", marginBottom: 16 }}>
        ← 목록
      </Link>
      <h1 style={{ marginTop: 0 }}>{product.name}</h1>
      <p style={{ fontSize: 20, fontWeight: 700 }}>
        가격: ₩ {Number(product.price).toLocaleString()}
      </p>
      <p>재고: {product.stock}</p>
    </div>
  );
}
