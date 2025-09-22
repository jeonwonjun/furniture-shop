import client from "./client";

export type Product = {
  id: number;
  name: string;
  price: number; // 백엔드가 long/number 반환한다고 가정
  stock: number;
};

export const fetchProducts = async () => {
  const res = await client.get<Product[]>("/api/products");
  return res.data;
};

export const fetchProduct = async (id: number) => {
  const res = await client.get<Product>(`/api/products/${id}`);
  return res.data;
};
