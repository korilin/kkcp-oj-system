import { marked } from "marked";

export function getDurationTime(duration) {
  if (duration == null) return "0h 0min"
  let h = parseInt(duration / 60)
  let min = duration - h * 60
  return `${h}h ${min}min`
}

export function resolveMarkdownAsHtml(md) {
  const html = marked(md);
  return html;
}
