import { Image } from '@tiptap/extension-image';
import { mergeAttributes } from '@tiptap/core';

/*export const CustomImage = Image.extend({
  renderHTML({ HTMLAttributes }) {
    return ['img', mergeAttributes(HTMLAttributes, { class: 'responsive-image' })];
  },

  parseHTML() {
    return [
      {
        tag: 'img[src]',
        getAttrs: (node) => {
          const element = node as HTMLImageElement;
          return {
            src: element.getAttribute('src'),
            alt: element.getAttribute('alt'),
            title: element.getAttribute('title'),
            class: 'responsive-image',
            
          };
        },
      },
    ];
  },

  addAttributes() {
    return {
      src: {
        default: null,
      },
      alt: {
        default: null,
      },
      title: {
        default: null,
      },
      class: {
        default: 'responsive-image',
        parseHTML: () => 'responsive-image',
        renderHTML: () => ({
          class: 'responsive-image',
        }),
      },
    };
  },

  
  
});*/


export const CustomImage = Image.extend({
    renderHTML({ HTMLAttributes }) {
      return ['img', mergeAttributes(HTMLAttributes, { class: 'responsive-image' })];
    },
  
    parseHTML() {
      return [
        {
          tag: 'img[src]',
          getAttrs: (node) => {
            const element = node as HTMLImageElement;
            // Ignore any inline style applied on drop by returning an empty style.
            return {
              src: element.getAttribute('src'),
              alt: element.getAttribute('alt'),
              title: element.getAttribute('title'),
              class: 'responsive-image',
              style: '', // <-- This ignores the ugly default inline styles.
            };
          },
        },
      ];
    },
  
    addAttributes() {
      return {
        src: {
          default: null,
        },
        alt: {
          default: null,
        },
        title: {
          default: null,
        },
        class: {
          default: 'responsive-image',
          parseHTML: () => 'responsive-image',
          renderHTML: () => ({ class: 'responsive-image' }),
        },
        style: {
          default: '',
          // Always ignore dropped inline styles
          parseHTML: () => '',
          // Render the style only if it's set (e.g. after resizing)
          renderHTML: (attributes) => (attributes['style'] ? { style: attributes['style'] } : {}),
        },
      };
    },
  });

