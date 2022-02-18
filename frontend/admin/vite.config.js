import { defineConfig } from "vite";
const { resolve } = require("path");
import vue from "@vitejs/plugin-vue";

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [vue()],
    base: "/admin/",
    publicDir: "../public",
    server: {
        fs: {
            // Allow serving files from one level up to the project root
            allow: [".."],
        },
    },
});
