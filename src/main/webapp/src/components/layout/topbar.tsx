import { useCallback, useEffect, useMemo, useState } from "react";
import { SidebarTrigger } from "../ui/sidebar";
import { Separator } from "../ui/separator";
import { Button } from "../ui/button";
import {
  Bell,
  Globe,
  Maximize,
  Minimize,
  Moon,
  Sun,
  Check,
} from "lucide-react";
import { motion, AnimatePresence } from "motion/react";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "../ui/dropdown-menu";
import { useTranslation } from "react-i18next";
import { useTheme } from "../theme-provider";
import AutoBreadcrumb from "../custom/auto-breadcrumb";
import { cn } from "@/lib/utils";
import { Tooltip, TooltipContent, TooltipTrigger } from "../ui/tooltip";

const iconButtonStyles = cn(
  "size-9",
  "text-muted-foreground hover:text-foreground",
  "hover:bg-accent/50",
  "transition-all duration-200",
  "focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2",
);

const languages = [
  { code: "en", label: "English" },
  { code: "fa", label: "فارسی" },
] as const;

export function TopBar() {
  const { theme, setTheme } = useTheme();
  const { t, i18n } = useTranslation();
  const dir = i18n.dir();

  const currentLang = i18n.resolvedLanguage ?? "fa";
  const isDark = theme === "dark";

  const [isFullscreen, setIsFullscreen] = useState(false);
  // Fullscreen state management
  useEffect(() => {
    const updateFullscreen = () =>
      setIsFullscreen(!!document.fullscreenElement);

    document.addEventListener("fullscreenchange", updateFullscreen);
    return () =>
      document.removeEventListener("fullscreenchange", updateFullscreen);
  }, []);

  const toggleFullscreen = useCallback(() => {
    if (document.fullscreenElement) {
      document.exitFullscreen();
    } else {
      document.documentElement.requestFullscreen();
    }
  }, []);

  const toggleTheme = useCallback(() => {
    setTheme(isDark ? "light" : "dark");
  }, [isDark, setTheme]);

  const changeLanguage = useCallback(
    (lang: string) => {
      i18n.changeLanguage(lang);
    },
    [i18n],
  );

  const menuAlign = useMemo(() => (dir === "rtl" ? "start" : "end"), [dir]);

  return (
    <header className="sticky top-0 z-10 flex h-16 shrink-0 items-center justify-between px-4 border-b bg-background/70 backdrop-blur supports-backdrop-filter:bg-background/60">
      {/* Left section */}
      <div className="flex items-center gap-2">
        <Tooltip>
          <TooltipTrigger asChild>
            <SidebarTrigger className={iconButtonStyles} />
          </TooltipTrigger>
          <TooltipContent side="bottom">Toggle sidebar</TooltipContent>
        </Tooltip>

        <Separator orientation="vertical" className="h-4" />

        <AutoBreadcrumb />
      </div>

      {/* Right section */}
      <nav className="flex items-center gap-1" aria-label="Toolbar">
        {/* Fullscreen toggle */}
        <Tooltip>
          <TooltipTrigger asChild>
            <Button
              variant="ghost"
              size="icon"
              className={cn(iconButtonStyles, "hidden md:inline-flex")}
              onClick={toggleFullscreen}
              aria-label={isFullscreen ? "Exit fullscreen" : "Enter fullscreen"}
            >
              <AnimatePresence mode="wait" initial={false}>
                <motion.div
                  key={isFullscreen ? "minimize" : "maximize"}
                  initial={{ opacity: 0, scale: 0.5 }}
                  animate={{ opacity: 1, scale: 1 }}
                  exit={{ opacity: 0, scale: 0.5 }}
                  transition={{ duration: 0.15 }}
                >
                  {isFullscreen ? (
                    <Minimize className="size-4" />
                  ) : (
                    <Maximize className="size-4" />
                  )}
                </motion.div>
              </AnimatePresence>
            </Button>
          </TooltipTrigger>
          <TooltipContent>
            {isFullscreen ? "Exit fullscreen" : "Enter fullscreen"}
          </TooltipContent>
        </Tooltip>

        {/* Theme toggle */}
        <Tooltip>
          <TooltipTrigger asChild>
            <Button
              variant="ghost"
              size="icon"
              className={iconButtonStyles}
              onClick={toggleTheme}
              aria-label={`Switch to ${isDark ? "light" : "dark"} mode`}
            >
              <motion.div
                key={theme}
                initial={{ rotate: -30, opacity: 0, scale: 0.8 }}
                animate={{ rotate: 0, opacity: 1, scale: 1 }}
                exit={{ rotate: 30, opacity: 0, scale: 0.8 }}
                transition={{ duration: 0.2, ease: "easeOut" }}
              >
                {isDark ? (
                  <Sun className="size-4" />
                ) : (
                  <Moon className="size-4" />
                )}
              </motion.div>
            </Button>
          </TooltipTrigger>
          <TooltipContent>
            Switch to {isDark ? "light" : "dark"} mode
          </TooltipContent>
        </Tooltip>

        {/* Language switcher */}
        <DropdownMenu>
          <Tooltip>
            <TooltipTrigger asChild>
              <DropdownMenuTrigger asChild>
                <Button
                  variant="ghost"
                  size="sm"
                  className="h-9 gap-2 px-3 text-muted-foreground hover:text-foreground hover:bg-accent/50 transition-all duration-200"
                >
                  <Globe className="size-4" />
                  <span className="text-xs font-medium uppercase tracking-wider">
                    {currentLang}
                  </span>
                </Button>
              </DropdownMenuTrigger>
            </TooltipTrigger>
            <TooltipContent>{t("change_language")}</TooltipContent>
          </Tooltip>

          <DropdownMenuContent
            align={menuAlign}
            className="w-40"
            sideOffset={8}
          >
            {languages.map(({ code, label }) => (
              <DropdownMenuItem
                key={code}
                onClick={() => changeLanguage(code)}
                className={cn(
                  "flex items-center justify-between",
                  currentLang === code && "bg-accent",
                )}
              >
                <span className={currentLang === code ? "font-medium" : ""}>
                  {label}
                </span>
                {currentLang === code && (
                  <Check className="size-4 text-primary" />
                )}
              </DropdownMenuItem>
            ))}
          </DropdownMenuContent>
        </DropdownMenu>

        {/* Notifications */}
        <Tooltip>
          <TooltipTrigger asChild>
            <Button
              variant="ghost"
              size="icon"
              className={cn(iconButtonStyles, "relative")}
              aria-label="Notifications"
            >
              <Bell className="size-4" />
              <span
                className="absolute top-1.5 right-1.5 size-2 rounded-full bg-destructive ring-2 ring-background"
                aria-hidden="true"
              />
            </Button>
          </TooltipTrigger>
          <TooltipContent>Notifications</TooltipContent>
        </Tooltip>
      </nav>
    </header>
  );
}
