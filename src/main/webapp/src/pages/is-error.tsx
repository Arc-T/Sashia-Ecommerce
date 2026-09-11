import { Button } from "@/components/ui/button";
import { ArrowLeft, ArrowRight, Info } from "lucide-react";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router";

export function CustomIsErrorPage() {
  const { t, i18n } = useTranslation();
  const navigate = useNavigate();
  return (
    <div className="flex flex-col items-center justify-center rounded-2xl border border-destructive/20 bg-destructive/5 p-12 text-center">
      <div className="rounded-full bg-destructive/10 p-4">
        <Info className="h-8 w-8 text-destructive" />
      </div>
      <h3 className="mt-4 text-lg font-semibold text-destructive">
        {t("Failed to load attribute")}
      </h3>
      <p className="mt-2 text-sm text-muted-foreground">
        {t("Please try again or select a different attribute")}
      </p>
      <Button variant="outline" className="mt-4" onClick={() => navigate(-1)}>
        {i18n.dir() === "ltr" ? (
          <ArrowLeft className="h-4 w-4" />
        ) : (
          <ArrowRight className="h-4 w-4" />
        )}
        {t("back")}
      </Button>
    </div>
  );
}
