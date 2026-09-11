import { PenBoxIcon } from "lucide-react";
import { useTranslation } from "react-i18next";

interface Props {
  icon: React.ReactNode;
  title: string;
  resourceId?: string;
  resourceName?: string;
}

export default function PageHeader({ title, resourceName, resourceId }: Props) {
  const { t } = useTranslation();
  return (
    <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
      <div className="flex items-center gap-4">
        {/* Back Button */}
        {/*<Button
        variant="ghost"
        size="icon"
        onClick={() => navigate(-1)}
        className="h-10 w-10 shrink-0"
      >
        <ArrowLeft className="h-5 w-5" />
      </Button>*/}

        {/* Icon */}
        <div className="rounded-xl bg-primary/10 p-3">
          <PenBoxIcon className="h-6 w-6 text-primary" />
        </div>

        <div>
          <h2 className="text-2xl font-bold tracking-tight">
            {resourceName ? t(title) + ": " + resourceName : t(title)}
          </h2>
          <p className="flex items-center gap-2 text-sm text-muted-foreground">
            <span>{t("view_attribute_information")}</span>
            <span className="h-1 w-1 rounded-full bg-muted-foreground/30" />
            {resourceId && (
              <span className="font-mono text-xs">
                {t("id") + `:${resourceId}`}
              </span>
            )}
            {/*<Button
              variant="ghost"
              size="sm"
              className="h-6 px-2 text-xs"
              // onClick={handleCopyId}
            >*/}
            {/*{isCopied ? t("Copied!") : <Copy className="h-3 w-3" />}*/}
            {/*</Button>*/}
          </p>
        </div>
      </div>
    </div>
  );
}
