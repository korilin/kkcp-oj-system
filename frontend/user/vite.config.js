import { defineConfig } from "vite";
const { resolve } = require("path");
import vue from "@vitejs/plugin-vue";

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [vue()],
    build: {
        rollupOptions: {
            input: {
                user: resolve(__dirname, "index.html"),
                admin: resolve(__dirname, "admin/index.html"),
            },
        },
    },
});
