const BASE = "/api/cart";

export const cartApi = {
  async getCart() {
    const res = await fetch(BASE, { credentials: "include" });
    if (!res.ok) throw new Error("장바구니 조회 실패");
    return res.json();
  },
  async upsert(productId, quantity) {
    const res = await fetch(BASE, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      credentials: "include",
      body: JSON.stringify({ productId, quantity }),
    });
    if (!res.ok) throw new Error("장바구니 담기 실패");
    return res.json();
  },
  async remove(itemId) {
    const res = await fetch(`${BASE}/${itemId}`, {
      method: "DELETE",
      credentials: "include",
    });
    if (!res.ok) throw new Error("장바구니 아이템 삭제 실패");
  },
};
