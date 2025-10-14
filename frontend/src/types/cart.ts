export type CartItem = {
id: number; // cart_items.id
product: {
id: number; // product_id
name: string;
price: number; // KRW integer
imageUrl?: string;
};
quantity: number;
};


export type CartResponse = CartItem[];