import { createContext, useContext, useEffect, useMemo, useState } from "react";
import { cartApi } from "../api/cartApi";

const CartContext = createContext(null);

export const useCart = () => {
  const ctx = useContext(CartContext);
  if (!ctx) throw new Error("useCart는 CartProvider 내부에서만 사용하세요.");
  return ctx;
};

export function CartProvider({ children }) {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const refresh = async () => {
    try {
      setLoading(true);
      const data = await cartApi.getCart();
      setItems(data);
      setError(null);
    } catch (e) {
      setError(e.message || "장바구니 불러오기 오류");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    refresh();
  }, []);

  const add = async (productId, quantity = 1) => {
    try {
      setLoading(true);
      const data = await cartApi.upsert(productId, quantity);
      setItems(data);
      setError(null);
    } catch (e) {
      setError(e.message || "장바구니 담기 오류");
    } finally {
      setLoading(false);
    }
  };

  const setQuantity = async (item, nextQty) => {
    if (nextQty <= 0) return remove(item.id);
    try {
      setLoading(true);
      const data = await cartApi.upsert(item.product.id, nextQty);
      setItems(data);
      setError(null);
    } catch (e) {
      setError(e.message || "수량 변경 오류");
    } finally {
      setLoading(false);
    }
  };

  const remove = async (itemId) => {
    try {
      setLoading(true);
      await cartApi.remove(itemId);
      await refresh();
    } catch (e) {
      setError(e.message || "삭제 오류");
    } finally {
      setLoading(false);
    }
  };

  const totalPrice = useMemo(
    () => items.reduce((s, it) => s + it.product.price * it.quantity, 0),
    [items]
  );

  const value = { items, loading, error, refresh, add, setQuantity, remove, totalPrice };
  return <CartContext.Provider value={value}>{children}</CartContext.Provider>;
}
