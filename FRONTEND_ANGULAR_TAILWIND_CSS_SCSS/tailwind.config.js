/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      colors: {
        // Nossa paleta personalizada (Inspirada nas referências)
        primary: {
          DEFAULT: '#8b5cf6', // Roxo vibrante (estilo Violet 500)
          hover: '#7c3aed',   // Roxo um pouco mais escuro para botões ao passar o mouse
        },
        dark: {
          bg: '#0f172a',      // Fundo super escuro para a tela toda (estilo Slate 900)
          card: '#1e293b',    // Fundo um pouco mais claro para as caixas/cards (estilo Slate 800)
          border: '#334155',  // Cor sutil para bordas
        }
      }
    },
  },
  plugins: [],
}
