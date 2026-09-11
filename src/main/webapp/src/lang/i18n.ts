import i18n from "i18next";
import { initReactI18next } from "react-i18next";
import z from "zod";
import LanguageDetector from "i18next-browser-languagedetector";
import { en, fa } from "zod/v4/locales";
import { resources } from "@/types/lang";

i18n
  .use(initReactI18next)
  .use(LanguageDetector)
  .init({
    resources,
    fallbackLng: "en",
    supportedLngs: ["en", "fa"],
    interpolation: {
      escapeValue: false,
    },
    detection: {
      lookupLocalStorage: "lang",
      caches: ["localStorage"],
    },
    // react: {
    //   useSuspense: false,
    // },
  })
  .then(() => syncZodLocale(i18n.language));

function syncZodLocale(lang: string) {
  z.config(lang === "fa" ? fa() : en());
}

export default i18n;
