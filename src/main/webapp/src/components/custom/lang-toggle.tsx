import { Languages } from "lucide-react";
import { useEffect } from "react";
import { Button } from "../ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "../ui/dropdown-menu";
import i18n from "@/lang/i18n";

export function LangToggle() {
  const currentLang = i18n.resolvedLanguage || "fa";

  const changeLanguage = (lang: "en" | "fa") => {
    i18n.changeLanguage(lang);
    localStorage.setItem("locale", lang);
    document.documentElement.lang = lang;
    document.documentElement.dir = lang === "fa" ? "rtl" : "ltr";
  };

  // sync on mount
  useEffect(() => {
    document.documentElement.lang = currentLang;
    document.documentElement.dir = currentLang === "fa" ? "rtl" : "ltr";
  }, [currentLang]);

  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <Button variant="outline" size="icon">
          <Languages className="h-5 w-5" />
          <span className="sr-only">Change language</span>
        </Button>
      </DropdownMenuTrigger>

      <DropdownMenuContent align="end">
        <DropdownMenuItem
          onClick={() => changeLanguage("fa")}
          className={currentLang === "fa" ? "font-bold" : ""}
        >
          فارسی
        </DropdownMenuItem>

        <DropdownMenuItem
          onClick={() => changeLanguage("en")}
          className={currentLang === "en" ? "font-bold" : ""}
        >
          English
        </DropdownMenuItem>
      </DropdownMenuContent>
    </DropdownMenu>
  );
}
