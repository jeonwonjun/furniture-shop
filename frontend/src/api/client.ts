import axios from "axios";

const client = axios.create({
  baseURL: import.meta.env.VITE_API_BASE,
  // 필요 시 개발용 CORS 쿠키 등 옵션 추가
  // withCredentials: true,
});

export default client;
