import { useTranslation } from "react-i18next";
import { FolderOpen, ChevronRight, Grid3X3, List } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { useNavigate } from "react-router";

interface Props {
  header: string;
  title: string;
  description: string;
  backPath?: string;
  resourcePath?: string;
  resourceLabel?: string;
  className?: string;
}

export function CustomNoResourcePage({
  title,
  description,
  backPath,
  resourcePath,
  resourceLabel,
}: Props) {
  const { t } = useTranslation();
  const navigate = useNavigate();

  const handleBack = () => {
    if (backPath) {
      navigate(backPath);
    } else {
      navigate(-1);
    }
  };

  const handleSelectResource = () => {
    if (resourcePath) {
      navigate(resourcePath);
    }
  };

  return (
    <>
      <Card className="w-full max-w-full border-0 shadow-lg">
        <div className="flex flex-col items-center p-8 text-center md:p-12">
          {/* Back Button */}
          {/*<Button
            variant="ghost"
            size="sm"
            onClick={handleBack}
            className="self-start gap-2 text-muted-foreground hover:text-foreground"
          >
            <ArrowLeft className="h-4 w-4" />
            {t("Back")}
          </Button>*/}

          {/* Empty State Illustration */}
          <div className="relative mb-6">
            <div className="flex h-24 w-24 items-center justify-center rounded-full bg-primary/10">
              <FolderOpen className="h-12 w-12 text-primary" />
            </div>
            <div className="absolute -bottom-2 -right-2 rounded-full bg-background p-1">
              <div className="flex h-6 w-6 items-center justify-center rounded-full bg-primary text-[10px] font-bold text-primary-foreground">
                ?
              </div>
            </div>
          </div>

          {/* Content */}
          <h2 className="text-2xl font-bold tracking-tight">{t(title)}</h2>

          <p className="mt-3 max-w-md text-sm text-muted-foreground">
            {t(description)}
          </p>

          {/* Action Steps */}
          <div className="mt-8 w-full space-y-3">
            <div className="flex items-center gap-3 rounded-lg bg-muted/50 p-3 text-left">
              <div className="flex h-8 w-8 shrink-0 items-center justify-center rounded-full bg-primary/10 text-sm font-semibold text-primary">
                1
              </div>
              <span className="text-sm">{t("Go to the Resources page")}</span>
            </div>

            <div className="flex items-center gap-3 rounded-lg bg-muted/50 p-3 text-left">
              <div className="flex h-8 w-8 shrink-0 items-center justify-center rounded-full bg-primary/10 text-sm font-semibold text-primary">
                2
              </div>
              <span className="text-sm">
                {t("Select or create a resource")}
              </span>
            </div>
          </div>

          {/* Action Buttons */}
          <div className="mt-8 flex flex-col gap-3 sm:flex-row">
            {resourcePath && (
              <Button onClick={handleSelectResource} className="gap-2 px-6">
                <Grid3X3 className="h-4 w-4" />
                {resourceLabel || t("Browse Resources")}
                <ChevronRight className="h-4 w-4" />
              </Button>
            )}

            <Button variant="outline" onClick={handleBack} className="gap-2">
              <List className="h-4 w-4" />
              {t("View All")}
            </Button>
          </div>

          {/* Help Text */}
          <p className="mt-6 text-xs text-muted-foreground/60">
            {t("Tip: You can also use the sidebar navigation")}
          </p>
        </div>
      </Card>
    </>
  );
}
